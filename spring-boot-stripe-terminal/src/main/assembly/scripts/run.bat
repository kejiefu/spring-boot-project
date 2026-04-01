@echo off & setlocal enabledelayedexpansion
set BIN_DIR=%~dp0

::cnf
set APP_MAIN=com.mountain.stripe.terminal.StripeTerminalApplication

::run
cd %BIN_DIR%\..
  set APP_CP=""
  for %%f in (lib\*.jar) do set APP_CP=!APP_CP!;%%f

  java -Xms256m -Xmx512m -cp ".\runtime;%APP_CP%" %APP_MAIN%
cd %BIN_DIR%
