# Build

## Pre-requisite

* Java 17

## Testing

Run the following command to test the application:

```
gradlew clean check
```

> The preceding command must be run from the location where you've cloned the repository.

## Building

Run the following command to build the application:

```
gradlew jlink
```

> The preceding command must be run from the location where you've cloned the repository.

The output of this task will be in the following directory format:

```
build\client-credential-<VERSION>
```

From the preceding directory you can test the build by running the following batch file:

```
client-credential.bat
```

## Packaging

Run the following command to package the application:

```
gradlew packWin
```

> The preceding command must be run from the location where you've cloned the repository.

The output of this task will be in the following directory:

```
build\pack
```

The package will contain the a distributable zip file and doesn't require java to be installed on the target windows machine.