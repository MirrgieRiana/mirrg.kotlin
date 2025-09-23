# no function <init>

特定のコードを記述するとKotlin 1.7 Native環境では以下のエラーが出る。

```
e: java.lang.IllegalStateException: no function <init> in mirrg.kotlin.helium
```

https://youtrack.jetbrains.com/issue/KT-52795/K-JS-and-K-Native-IR-validation-compilation-errors-for-a-valid-kotlin-code?utm_source=chatgpt.com

この対策は、ローカル関数を作らないこと。

クラス直下のprivate関数なら問題ない。
