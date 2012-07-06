echo 'Stopping OpenLDAP...'
#slapd.exe -f C:\JavaTools\symas-openldap-gold-win\etc\openldap\slapd.conf
set prgId=type slapd.pid
set prgName=slapd.exe
taskkill -F /IM slapd.exe
@rem taskkill -F -pid %prgId% 
