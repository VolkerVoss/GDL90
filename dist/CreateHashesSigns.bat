set "version=1.0.0"

certutil -hashfile "gdl90-%version%.pom" MD5 | findstr /v "CertUtil:" | findstr /v "Hash" > gdl90-%version%.pom.md5
certutil -hashfile "gdl90-%version%.pom" SHA1 | findstr /v "CertUtil:" | findstr /v "Hash" > gdl90-%version%.pom.sha1
certutil -hashfile "gdl90-%version%.pom" SHA256 | findstr /v "CertUtil:" | findstr /v "Hash" > gdl90-%version%.pom.sha256
certutil -hashfile "gdl90-%version%.pom" SHA512 | findstr /v "CertUtil:" | findstr /v "Hash" > gdl90-%version%.pom.sha512
gpg --batch --yes -u "volker.v@gmx.net" -ab -o gdl90-%version%.pom.asc gdl90-%version%.pom

certutil -hashfile "gdl90-%version%.jar" MD5 | findstr /v "CertUtil:" | findstr /v "Hash" > gdl90-%version%.jar.md5
certutil -hashfile "gdl90-%version%.jar" SHA1 | findstr /v "CertUtil:" | findstr /v "Hash" > gdl90-%version%.jar.sha1
certutil -hashfile "gdl90-%version%.jar" SHA256 | findstr /v "CertUtil:" | findstr /v "Hash" > gdl90-%version%.jar.sha256
certutil -hashfile "gdl90-%version%.jar" SHA512 | findstr /v "CertUtil:" | findstr /v "Hash" > gdl90-%version%.jar.sha512
gpg --batch --yes -u "volker.v@gmx.net" -ab -o gdl90-%version%.jar.asc gdl90-%version%.jar

certutil -hashfile "gdl90-%version%-sources.jar" MD5 | findstr /v "CertUtil:" | findstr /v "Hash" > gdl90-%version%-sources.jar.md5
certutil -hashfile "gdl90-%version%-sources.jar" SHA1 | findstr /v "CertUtil:" | findstr /v "Hash" > gdl90-%version%-sources.jar.sha1
certutil -hashfile "gdl90-%version%-sources.jar" SHA256 | findstr /v "CertUtil:" | findstr /v "Hash" > gdl90-%version%-sources.jar.sha256
certutil -hashfile "gdl90-%version%-sources.jar" SHA512 | findstr /v "CertUtil:" | findstr /v "Hash" > gdl90-%version%-sources.jar.sha512
gpg --batch --yes -u "volker.v@gmx.net" -ab -o gdl90-%version%-sources.jar.asc gdl90-%version%-sources.jar


certutil -hashfile "gdl90-%version%-javadoc.jar" MD5 | findstr /v "CertUtil:" | findstr /v "Hash" > gdl90-%version%-javadoc.jar.md5
certutil -hashfile "gdl90-%version%-javadoc.jar" SHA1 | findstr /v "CertUtil:" | findstr /v "Hash" > gdl90-%version%-javadoc.jar.sha1
certutil -hashfile "gdl90-%version%-javadoc.jar" SHA256 | findstr /v "CertUtil:" | findstr /v "Hash" > gdl90-%version%-javadoc.jar.sha256
certutil -hashfile "gdl90-%version%-javadoc.jar" SHA512 | findstr /v "CertUtil:" | findstr /v "Hash" > gdl90-%version%-javadoc.jar.sha512
gpg --batch --yes -u "volker.v@gmx.net" -ab -o gdl90-%version%-javadoc.jar.asc gdl90-%version%-javadoc.jar
