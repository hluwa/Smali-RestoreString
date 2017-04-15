字符串解密

适用加密方法单String参数，返回类型为String。

有的字符串不能处理是因为编码坑爹。。

ubuntu下测试正常，其余未测。

**注：使用baksmali d 和smali a，不要使用dex2jar里的smali和baksmali。**

use:

java -jar restore.jar \<jarfile\> \<smalidir\> \<methodsig\>

例:
java -jar restore.jar classes-dex2jar.jar out Lcom/inca/security/coM6;->goto
