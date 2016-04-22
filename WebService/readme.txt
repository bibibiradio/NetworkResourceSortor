1，将servlet-api.jar复制到${JAVA_HOME}/jdk${VERSION}/jre/ext/下
2，解压缩tomcat zip包
3，修改server.xml中<host>标签内的<context>标签中的docBase属性等于FrontPage目录路径
4，用修改后的server.xml覆盖tomcat目录中conf/server.xml
5，在Service目录执行mvn clean package
6，将mvn生成的war包放置tomcat的webapps目录下
7，重命名webapps下该war为ROOT.war
8，启动tomcat，执行tomcat下startup.sh或startup.bat
9，调用http://127.0.0.1:12315/frontpage/hello.html和http://127.0.0.1:12315/hello.do