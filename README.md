### StartReport
#### Send email(with/without specified file) to specified address on start
`mvn package assembly:single`
### TODO:
- Detect file name set in the config and use it for sending mail
- Add the ability to use specified config file (like `xxx.jar -c=./cfg.properties`)
- Add options in cmdline to override config or use it without config file (like `xxx.jar -name=MCUmbrella -pswd=123456 -smtp=smtp.qq.com etc.`) *(could be abandoned)*
