#!/bin/sh
# kael use for version incremental file zip generate and time kill...

# function implement
function pack()
{
    if [ $# -ne 1 ] ; then
        echo "wrong param"
        exit -1
    else
        if [ $1 -eq 1 ] ; then
            echo "only one version"
            exit 0
        else
            # delete the origin version zip
            rm *-to-*.zip
            for (( var=1 ; var <= $1 ; var++))
            do
                let left=$1-$var
                let upper=$var+1
                if [ $left -ne 0 ] ; then
                    version_now=`tail -n $var tmpstep.file | head -n 1`
                    version_upper=`tail -n $upper tmpstep.file | head -n 1`

                    echo $version_upper
                    packTwoVersion $version_now $version_upper
                fi
            done
        fi
    fi
}

function packTwoVersion(){
    if [ $# -ne 2 ] ; then
        echo "wrong param"
        exit -1
    else
        # combine version name string
        version_code=${1}'-to-'${2}'.zip'
        version_deletefile=${1}'-to-'${2}'.del'
        # > is new add,>> is append so this is not need
        #if [ -e $version_deletefile ] ; then
        #                       rm $version_deletefile
        #                   fi
        # call git diff get diff files and zip.
        # if deleted file,we need save to a file for information
        # out put to version.del and zip into zipball,then delete this file
        git diff --name-only $1 $2 | xargs zip $version_code | grep "zip warning: name not matched: " | awk 'NF==6 {print $6}' > $version_deletefile
        zip $version_code $version_deletefile
        rm $version_deletefile
    fi
}

function packWithVersion()
{
    packTwoVersion $1 $2
}

function packAll()
{
    # get latest version
    # `` for run a command and get result to a varible
    # $? is last call's function return code
    # no space here
    count=`wc -l tmpstep.file | awk '{print $1}'`
    # wc -l will count \n here,so need add one element count,and let it be a number
    let sum=$count+1
    pack $sum
}

# first ,do some error checking
if [ $# -lt 1 ] ; then
    echo "Usage is ./versionToolStep.sh <directory-name> or ./versionToolStep.sh <directory-name> targetversion targetversion2"
    exit -1
fi

if [ ! -e $1 ] ; then
    echo "Directory not exist"
    exit -1
fi

if [ ! -d $1 ] ; then
    echo "Target must be a directory"
    exit -1
fi

cd $1
# next check git command is exist

# use git log first and redirect stderr to stdout and save
# if not error,the result of git log will put to file too
git log --pretty=format:"%h" > tmpstep.file 2>&1

# check the result is ok,0 is ok
if [ $? -ne 0 ] ; then
    # if got error,display error and quit
    cat tmpstep.file
else
    if [ $# -ne 1 ] ; then
        if [ $# -ne 3 ] ; then
            echo "Usage is ./versionToolStep.sh <directory-name> or ./versionToolStep.sh <directory-name> targetversion targetversion2"
            exit -1
        else
            packWithVersion $2 $3
        fi
    else
        packAll
    fi
fi

rm tmpstep.file