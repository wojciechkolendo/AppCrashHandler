# AppCrashHandler

Shows uncaught exceptions in Alert Dialog

# Usage

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
