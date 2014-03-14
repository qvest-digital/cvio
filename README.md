cvmanager [![Build Status](https://travis-ci.org/tarent/cvio.png)](https://travis-ci.org/tarent/cvio)
=========

Web application for management of skills and personal cv's. 



.deb packaging
===============

To create a debian package do the following:
```
git checkout debian
git merge master     #if you have changes in master
mvn clean package
dpkg-buildpackage
```

(It would be better cleaner to trigger the maven build
from the debian build process, but because of the fakeroot
this runs into an error)

In the result, you will get a package in the parent directory.
Install it with:
        
```
sudo dpkg -i ../cvio_0.1.0_all.deb
sudo apt-get install -f      #for the dependencies
```

After that, you have an installed and running application on port __5050__.
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
