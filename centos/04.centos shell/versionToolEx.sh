#!/bin/sh
# kael use for version incremental file zip generate,file sort and time kill...
# 2015-04-26

# function implement
function pack()
{
    if [ $# -ne 1 ] ; then
        echo 'wrong param'
        exit -1
    else
        if [ $1 -eq 1 ] ; then
            echo "only one version"
            exit 0
        else
            # delete the origin version zip
            rm -f *-to-*.zip
            version_latest=`head -n 1 tmp.file`
            version_tag_latest=`head -n 1 tagtmp.file`
            for (( var=1 ; var <= $1 ; var++))
            do
                #awk -v cur=$var 'NR==cur {print $1}' tmp.file
                let left=$1-$var
                if [ $left -ne 0 ] ; then
                    version_now=`tail -n $var tmp.file | head -n 1`
                    # combine version name string,version name format is tag.command_sha1
                    version_tag_now=`tail -n $var tagtmp.file | head -n 1`
                    version_code=${version_tag_now}'.'${version_now}'-to-'${version_tag_latest}'.'${version_latest}'.zip'
                    # no use for now,use del.file instead,may not pack into zip
                    #version_deletefile=${version_now}'-to-'${version_latest}'.del'
                    # > is new add,>> is append so this is not need
                    #if [ -e $version_deletefile ] ; then
                    #                       rm $version_deletefile
                    #                   fi
                    # call git diff get diff files and zip.
                    # if deleted file,we need save to a file for information
                    # out put to version.del and zip into zipball,then delete this file
                    # save diff into file
                    git diff --name-only $version_now $version_latest > diff.file
                    cat diff.file | xargs zip $version_code | grep "zip warning: name not matched: " | awk 'NF==6 {print $6}' > del.file
                    # -F is set the splitter
                    # awk -F: 'NF==3 {print $3}'
                    # take off repeat line both in diff.file and $version_deletefile,so the remain line will be the two version's modify/add file
                    # sorted and delete the repeat line by uniq -u
                    cat diff.file $version_deletefile | sort | uniq -u > a.file
                    # generate a update.json by shell,use for update the local ures.json
                    echo '{' > update.json
                    echo '"game_version":"'${version_tag_latest}'.'${version_latest}'",' >> update.json
                    echo '"engine_version":"'${engineversion}'",' >> update.json
                    echo '"updateUrl":'${serverpath}',' >> update.json
                    echo '"fix_array":{' >> update.json
                    # \x22 for "
                    # not want last "," ,so we use awk array save every line and line count finally make the output by if else
                    cat a.file | awk '{a[NR]=$0} END{for(i=1;i<=NR;i++)if(i!=NR){print "\x22"a[i]"\x22:1,"}else{print "\x22"a[i]"\x22:1"}}' >> update.json
                    echo '}}' >> update.json

                    zip $version_code update.json
                    #zip $version_code $version_deletefile ures.js
                    #rm $version_deletefile
                fi
            done
            # generate the latest versionFile format:1.0.14.r1.abcdefg_engineversion
            echo ${version_tag_latest}'.'${version_latest}'_'${engineversion} > v
            # keep only first one line for usage,more than one line cause curl error...
            awk '{{printf"%s",$0}}' v > version
            rm -f v
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

# engineversion,server path setting here, not for param input now
engineversion='3.5'
serverpath='"http://kaeltomato.sinaapp.com"'

#if [ $2 ]; then
#    let engineversion=$2
#fi

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
    # wc -l will count \n here,git log will have one less \n in command result but not in tag command,so need add one element count,and let it be a number
    let sum=$count+1
    # check tag and commit count is match,tag command's data is order by created time,so we use sort to reverse it
    git tag --list | sort -r > tagtmp.file
    tagcount=`wc -l tagtmp.file | awk '{print $1}'`
    if [ $sum -ne $tagcount ]; then
        echo "missing tag for some commit"
    else
        # create a temp file for save each two commit's different file
        touch diff.file update.json a.file del.file
        pack $sum
    fi
fi

# finally we do not need it
rm tmp.file
rm tagtmp.file
rm diff.file update.json a.file del.file