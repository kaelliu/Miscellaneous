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
            version_latest=`head -n 1 tmp.file`
            for (( var=1 ; var <= $1 ; var++))
            do
                #awk -v cur=$var 'NR==cur {print $1}' tmp.file
                let left=$1-$var
                if [ $left -ne 0 ] ; then
                    version_now=`tail -n $var tmp.file | head -n 1`
                    # combine version name string
                    version_code=${version_now}'to'${version_latest}'.zip'
                    version_deletefile=${version_now}'to'${version_latest}'.del'
                    # > is new add,>> is append so this is not need
                    #if [ -e $version_deletefile ] ; then
                    #                       rm $version_deletefile
                    #                   fi
                    # call git diff get diff files and zip.
                    # if deleted file,we need save to a file for information
                    # out put to version.del and zip into zipball,then delete this file
                    git diff --name-only $version_now $version_latest | xargs zip $version_code | grep "zip warning: name not matched: " | awk 'NF==6 {print $6}' > $version_deletefile | xargs zip $version_code
                        # -F is set the splitter
                        # awk -F: 'NF==3 {print $3}'
                    rm $version_deletefile
                fi
            done
        fi
    fi
}

# first ,do some error checking
if [ $# -ne 1 ] ; then
    echo "Usage is ./versionTool.sh <directory-name>"
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
git log --pretty=format:"%h" > tmp.file 2>&1

# check the result is ok,0 is ok
if [ $? -ne 0 ] ; then
    # if got error,display error and quit
    cat tmp.file
else
    # get latest version
    # `` for run a command and get result to a varible
    # $? is last call's function return code
    # no space here
    count=`wc -l tmp.file | awk '{print $1}'`
    # wc -l will count \n here,so need add one element count,and let it be a number
    let sum=$count+1
    pack $sum
fi

# finally we do not need it
rm tmp.file
