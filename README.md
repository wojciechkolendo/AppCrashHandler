# AppCrashHandler

Shows uncaught exceptions in Alert Dialog

[ ![Download](https://api.bintray.com/packages/wojciechkolendo/maven/AppCrashHandler/images/download.svg) ](https://bintray.com/wojciechkolendo/maven/AppCrashHandler/_latestVersion)

### Gradle Dependency (jCenter)

```Gradle
dependencies {
    implementation 'com.wojciechkolendo:appcrashhandler:1.0.0'
}
```

### Usage

Add to your Application subclass:

```java
@Override
public void onCreate() {
	super.onCreate();
	if (BuildConfig.DEBUG) {
		AppCrashHandler.setAsDefault();
	}
}
```
