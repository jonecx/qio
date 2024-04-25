#!/bin/bash

# Generate a random port number for gRPC communication
GRPC_PORT=$(shuf -i 50000-60000 -n 1)

# 1. Install and setup an emulator
echo "Installing emulator..."
$ANDROID_HOME/cmdline-tools/latest/bin/sdkmanager --install "emulator"

# 2. Create AVD
echo "Creating AVD..."
$ANDROID_HOME/cmdline-tools/latest/bin/avdmanager create avd --force -n "Pixel_5_AVD" --abi arm64-v8a --device "pixel_5" --package "system-images;android-34;google_apis;arm64-v8a"

# Check if the emulator already exists
echo "Creating emulator..."
#$ANDROID_HOME/emulator/emulator -avd Pixel_5_AVD -no-audio -no-window --force &
$ANDROID_HOME/emulator/emulator -avd Pixel_5_AVD -no-snapshot-load -grpc $GRPC_PORT
EMULATOR_PID=$!

# 3. List available AVDs
$ANDROID_HOME/cmdline-tools/latest/bin/avdmanager list avd

# 4. Wait for the emulator to boot up
echo "Waiting for emulator to boot up..."
$ANDROID_HOME/platform-tools/adb wait-for-device shell 'while [[ -z $(getprop sys.boot_completed) ]]; do sleep 1; done'
echo "Emulator boot completed."

# 5. Install the app
echo "Installing the app..."
$ANDROID_HOME/platform-tools/adb install -r app/build/outputs/apk/debug/app-debug.apk

# TODO
# 6. Start UIAutomator test
echo "Starting UIAutomator test..."
# Example command to start UIAutomator test
# $ANDROID_HOME/platform-tools/adb shell uiautomator runtest <path_to_test_jar_file> -c <test_class_name>

# 7. Clean up
echo "Cleaning up..."
kill -9 $EMULATOR_PID
