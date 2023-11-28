pluginManagement {
	repositories {
		maven { setUrl("https://maven.aliyun.com/repository/gradle-plugin") }
		maven { setUrl("https://maven.aliyun.com/repository/public") } // central 和 jcenter 的聚合仓
		maven { setUrl("https://maven.aliyun.com/repository/google") }
		maven { setUrl("https://jitpack.io") }
		google() // 阿里云对于有些库会有丢失，加入进行兜底
		mavenCentral()
		gradlePluginPortal()
	}
}
dependencyResolutionManagement {
	repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
	repositories {
		mavenLocal()
		maven { setUrl("https://maven.aliyun.com/repository/public") } // central 和 jcenter 的聚合仓
		maven { setUrl("https://maven.aliyun.com/repository/google") }
		maven { setUrl("https://jitpack.io") }
		google() // 阿里云对于有些库会有丢失，加入进行兜底
		mavenCentral()
	}
}

rootProject.name = "GmTools-Android"
include(":app")

include(":GmTools")
project(":GmTools").projectDir = file("./GmTools")
 