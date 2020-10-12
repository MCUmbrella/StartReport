## StartReport
#### Send email(with/without specified file) to specified address on start
### Build:
`mvn package assembly:single`
### Download: [HERE](https://github.com/mcumbrella/startreport/releases)
### Wiki: [HERE](https://github.com/MCUmbrella/StartReport/wiki)
### Usage:
`java -jar xxx.jar [-c=<path to config file>] [-d]`
- **-c**: Set the config file that StartReport should use. Default is "cfg.properties"
- **-d**: Turn on debug mode. It will output some config values(include password) and JavaMail's debug messages
### TODO:
- Add options in cmdline to override config or use it without config file (like `xxx.jar -name=MCUmbrella -pswd=123456 -smtp=smtp.qq.com etc.`) *(could be abandoned)*
### cfg.properties:
- **ver**: Config format version. Dont change if u dont know how to update config format manually
- **user**: Your sender email address
- **pswd**: Passwd used for sign into ur email account
- **to**: Where to send. Write a email address here
- **file**: Which file u want to send. Keep it blank if u dont want to send file
- **smtp**: SMTP server used for sending mail
- **subj**: Email subject
- **body**: Email content. HTML supported
