# mavendeployplugin

TLD generator from annotations on `SimpleTagSupport` classes.

## How to create new version of plugin

1. Let's say git repository is cloned into local folder `C:\tld-generator`.
1. Clone repository to separate folder, e.g. `C:\mvn-repo`.
1. Switch to `mvn-repo` branch in that folder.
1. Go back to folder `C:\tld-generator`
1. First, change the version `mvn versions:set -DnewVersion=1.29 versions:commit`
1. Then, if no new modules were added run `mvn clean install -DmvnRepo=C:\\mvn-repo` (here and later: \\ is escaped for Cygwin).
1. If a new module was added, then do this:
   1. Run  `mvn clean install -DmvnRepoNewModule=C:\\mvn-repo`. 
   1. Revert changed files in mvn-repo folder. Don't revert (i.e. delete) new ones.
   1. Rename `maven-metadata-local*` to `maven-metadata*`. 
   1. Run the same line as for the case when no new modules were added.
1. Go to folder `C:\mvn-repo`.
1. Add new files to commit, then commit and push `mvn-repo` branch to server. 

## Maven: how to build and install locally, without uploading to remote repository

    mvn clean install
