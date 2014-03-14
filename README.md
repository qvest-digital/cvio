cvmanager [![Build Status](https://travis-ci.org/tarent/cvio.png)](https://travis-ci.org/tarent/cvio)
=========

Web application for management of skills and personal cv's. 

Building and running the service
---------------------------------
You need __maven__ (>=2.2) and a __jdk__ (>=7).
```
git clone https://github.com/tarent/cvio.git
cd cvio
mvn package
java -jar target/cvserver-*.jar server config.yaml 
firefox http://127.0.0.1:8080
```


.deb packaging
-----------------
It is easy to create a .deb package of the application. This
package was tested under ubunut 13.10 and should work with others,
but it is faw away from being an debian-policy complient package.

To create a debian package do the following:
```
git checkout debian     # change to the debian branch
git merge master        # if you have changes in master
mvn clean package       # build the java package *
dpkg-buildpackage       # build the debian package
```

(* It would be better cleaner to trigger the maven build
from the debian build process, but because of the fakeroot
this runs into an error)

In the result, you will get a package in the parent directory.

.deb installation
-------------------

Install the package with:
        
```
sudo dpkg -i ../cvio_0.1.0_all.deb
sudo apt-get install -f      #for the dependencies
```

After that, you have an installed and running application on port __5050__.
At port __5051__ the service has opened a monitoring interface. It is secured my
username/password from the config.yaml 

managing the service 
-----------------------
Starting and stoping ist managed by the linux daemon-tools, as follows:

```
sudo svstat /etc/service/cvio/   # status of the service
sudo svc -d /etc/service/cvio/   # shut the service down
sudo svc -u /etc/service/cvio/   # starts the service
sudo svc -du /etc/service/cvio   # restart the service
```

The service ist installed at the folloging locations:
```
dpkg -L cvio    # shows you the installed files
/var/log/cvio/cvio.log      # application log
/var/log/cvio/request.log   # http request log
/var/log/cvio/cvio.out      # startup message an System.out/err of the service
/var/lib/cvio/data          # elastic search data directory
/etc/cvio/deamon_options    # JVM settings
/etc/cvio/config.yaml       # service configuration
```

