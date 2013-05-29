#!/usr/bin/env bash
if [ ! -f install ]; then
echo 'install must be run within its container folder' 1>&2
exit 1
fi

# ` not '
CURDIR=`pwd`
THEGOPATH=$GOPATH
cd $GOPATH
gofmt -w $CURDIR
# set your project folder to compile hered
go install com.kael/testgo/main
echo 'finished'
