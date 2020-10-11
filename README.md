### StartReport
#### Send email(with/without specified file) to specified address on start
`mvn package assembly:single`
### TODO:
- Detect file name set in the config and use it for sending mail
- Add the ability to use specified config file (like `xxx.jar -c=./cfg.properties`)
- Add options in cmdline to override config or use it without config file (like `xxx.jar -name=MCUmbrella -pswd=123456 -smtp=smtp.qq.com etc.`) *(could be abandoned)*
### cfg.properties:
`
ver: Config format version. Dont change if u dont know how to update config format manually
user: Your sender email address
pswd: Passwd used for sign into ur email account
to: Where to send. Write a email address here
file: Which file u want to send. Keep it blank if u dont want to send file
smtp: SMTP server used for sending mail
subj: Email subject
body: Email content. HTML supported
`
