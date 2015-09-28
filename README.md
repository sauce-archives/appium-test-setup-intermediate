[![Build Status](https://travis-ci.org/testobject/appium-test-setup-intermediate.svg)](https://travis-ci.org/testobject/appium-test-setup-intermediate)

# appium-test-setup-intermediate
A setup that allows you to run tests on the TestObject platform, exemplified through two basic tests on the [Calculator app](https://github.com/aluedeke/calculator) by Andreas Lüdeke. It differs from the [basic setup](https://github.com/testobject/appium-test-setup-basic/) in that it registers the results of said tests so the user can see them through the online UI.

# setting up travis ci
1. add a .travis.yml to your project  
2. add your testobject_api_key as encrypted enviroment variables  
```
travis encrypt TESTOBJECT_API_KEY=YOUR_API_KEY --add env.global
```
