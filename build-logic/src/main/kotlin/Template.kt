import org.eclipse.aether.util.version.GenericVersionScheme
import org.eclipse.aether.version.VersionScheme

object Template {

    class Arguments(val versionString: String)

    abstract class Node {
        abstract fun evaluate(arguments: Arguments): List<String>
    }


    private class LinesLiteralNode(val lines: List<String>) : Node() {
        override fun evaluate(arguments: Arguments) = lines
    }

    private fun parseLinesLiteral(input: MutableList<String>): Node {
        val lines = mutableListOf<String>()

        while (true) {
            val line = input.firstOrNull()
            if (line == null || line.startsWith("#")) break
            lines += line
            input.removeFirst()
        }

        return LinesLiteralNode(lines)
    }

    private class ErrorNode : Node() {
        override fun evaluate(arguments: Arguments) = throw RuntimeException("Generation failed")
    }

    private fun parseBlock(input: MutableList<String>): Node {
        val line = input.firstOrNull()
        if (line == "#error") {
            input.removeFirst()
            return ErrorNode()
        }
        return parseLinesLiteral(input)
    }

    private class IfSection(val versionRangeString: String?, val body: Node)

    private class IfNode(val sections: List<IfSection>) : Node() {
        override fun evaluate(arguments: Arguments): List<String> {
            sections.forEach {
                val ok = if (it.versionRangeString != null) {
                    val versionScheme: VersionScheme = GenericVersionScheme()
                    val versionRange = versionScheme.parseVersionRange(it.versionRangeString)
                    val version = versionScheme.parseVersion(arguments.versionString)
                    versionRange.containsVersion(version)
                } else {
                    true
                }
                if (ok) return it.body.evaluate(arguments)
            }
            return listOf()
        }
    }

    private fun tryParseIf(input: MutableList<String>): Node? {
        val sections = mutableListOf<IfSection>()

        run {
            val line = input.firstOrNull()
            if (line == null || !line.startsWith("#if ")) return null
            val versionRangeString = line.drop(4)
            input.removeFirst()
            val body = parseBlock(input)
            sections += IfSection(versionRangeString, body)
        }

        while (true) {
            val line = input.firstOrNull()
            if (line == null || !line.startsWith("#elseif ")) break
            val versionRangeString = line.drop(8)
            input.removeFirst()
            val body = parseBlock(input)
            sections += IfSection(versionRangeString, body)
        }

        run {
            val line = input.firstOrNull()
            if (line == null || line != "#else") return@run
            input.removeFirst()
            val body = parseBlock(input)
            sections += IfSection(null, body)
        }

        run {
            val line = input.firstOrNull()
            check(line != null && line == "#endif")
            input.removeFirst()
        }

        return IfNode(sections)
    }

    private class RootResult(val head: Node, val tail: List<Pair<Node, Node>>) : Node() {
        override fun evaluate(arguments: Arguments): List<String> {
            val lines = mutableListOf<String>()
            lines += head.evaluate(arguments)
            tail.forEach {
                lines += it.first.evaluate(arguments)
                lines += it.second.evaluate(arguments)
            }
            return lines
        }
    }

    private fun parseRoot(input: MutableList<String>): Node {
        val head = parseBlock(input)
        val tail = mutableListOf<Pair<Node, Node>>()
        while (true) {
            val ifResult = tryParseIf(input)
            if (ifResult == null) break
            val body = parseBlock(input)
            tail += Pair(ifResult, body)
        }
        return RootResult(head, tail)
    }


    fun parse(lines: List<String>): Node {
        val input = lines.toMutableList()
        val node = parseRoot(input)
        check(input.isEmpty())
        return node
    }

    fun evaluate(string: String, arguments: Arguments): String {
        val input = string.replace("\r", "").split("\n")
        val output = parse(input).evaluate(arguments)
        return output.joinToString("\n")
    }

}
