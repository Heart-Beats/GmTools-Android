# GmTools-Android

Android 平台开发可以使用的国密 Https 工具库


------



### 1. 使用

 目前支持如下两种网络请求库：

1. **OkHttp**

   调用 `OkHttpTools.supportGM(...)` 相关方法，即可得到一个支持国密协议请求的 `OkHttpClient.Builder` 对象，随后正常使用 OkHttp 即可。

2. **HttpsURLConnection**

   - GET

     `HttpsURLConnectionTools.get(...)` 方法即可发起 GET 请求。

   - POST

     `HttpsURLConnectionTools.post(...)` 方法即可发起 POST 请求。



------



### 2. Gradle 引入

- 添加仓库地址

  在项目根目录中的 `build.gradle` 或 `settings.gradle` 中添加如下仓库地址：

  ```groovy
  repositories {
      ...
      maven { url 'https://jitpack.io' }
  }
  ```

  

- 引入依赖

  在需要使用 JNA 的 Module 中添加如下依赖：

  ```groovy
  dependencies {
          implementation "com.github.Heart-Beats:GmTools-Android:$Tag"
  }
  ```

  <center>Tag：<a href ="https://jitpack.io/#Heart-Beats/GmTools-Android"><image src ="https://jitpack.io/v/Heart-Beats/GmTools-Android.svg" alt="Release"/> </a></center>

