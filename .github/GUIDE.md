# GitHub Actions Workflow Explanation

This project uses GitHub Actions to automate publishing to Maven Central. The project uses the `com.vanniktech.maven.publish` plugin to handle the Maven Central publishing process.

## Configuring Secrets

Before using the workflow, ensure that the following secrets are added to the GitHub repository settings:

- `OSSRH_USERNAME`: Maven Central (Sonatype OSSRH) username
- `OSSRH_PASSWORD`: Maven Central (Sonatype OSSRH) password
- `SIGNING_KEY_ID`: GPG signing key ID (last 8 digits)
- `SIGNING_PASSWORD`: GPG signing key password
- `GPG_KEY_CONTENTS`: Base64-encoded GPG private key (use the command: `gpg --export-secret-keys YOUR_KEY_ID | base64 -w 0`)
- `SONATYPE_STAGING_PROFILE_ID`: Sonatype staging profile ID (optional)

These environment variables are mapped to the corresponding Gradle properties as required by the `com.vanniktech.maven.publish` plugin.

## Publishing Environment

The publishing workflow for this project runs on the `macOS-latest` runner to support the iOS (iosArm64) target platform. The workflow automatically sets up:

- JDK 17
- The latest stable version of Xcode
- Android SDK
- Necessary build and signing tools
- GPG tools for build signing

## Publishing Process

### Automatic Publishing (Based on Tags)

1. Create a new version tag, e.g., `v0.1.9`:
   ```
   git tag -a v0.1.9 -m "Release v0.1.9"
   git push origin v0.1.9
   ```

2. After pushing the tag, the `release.yml` workflow will be automatically triggered, which will:
   - Extract the version number from the tag
   - Update the version in `gradle.properties`
   - Create a GitHub Release

3. After creating the Release, the `publish.yml` workflow will be automatically triggered, which will:
   - Build the project (including the iOS platform)
   - Sign the build artifacts using GPG
   - Publish all artifacts to Maven Central using the `com.vanniktech.maven.publish` plugin

### Manual Publishing

1. On the GitHub repository page, go to the "Actions" tab
2. Select the "Publish to Maven Central" workflow
3. Click the "Run workflow" button
4. Select the branch to publish from the dropdown menu
5. Click the "Run workflow" button to start the publishing process

## Notes

- Ensure that the version number in `gradle.properties` is correctly formatted (if it's a snapshot version, it should end with `-SNAPSHOT`)
- Test your library before publishing to ensure quality
- Check the Sonatype repository to verify that the publishing was successful
- Building for the iOS platform requires a macOS environment and Xcode tools, which is why we use the macOS runner in GitHub Actions
- The `com.vanniktech.maven.publish` plugin automatically handles most of the Maven Central publishing process, including signing and uploading
