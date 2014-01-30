
                            SVN Importer 1.0.XX
                          =======================
                          
0) INTRODUCTION

SVNImporter is tool for import of content of other repositories (CVS, PCVS, MKS,
ClearCase, Visual Source Safe) into the Subversion (SVN). It analyzes the history
of the source repository and groups the corresponding commits into one SVN Revision.


1) INSTALLATION

Unpack the distribution archive to a desired location. It has the following 
content:

        lib/               External and modules libraries
        config.properties  The sample config file
        svnimporter.jar    The executable jar file
        run.bat            Convenience script for Win: java -jar svnimporter.jar %1 %2
        readme.txt         (this file)
        license.txt        The license file

2) USAGE

The SVNImporter works in two phases: first, it creates the dump file from the source 
repository, second, it imports the dump into the SVN.

To launch SVNImporter type

        java -jar svnimporter.jar
        or run.bat
        
or execute the run.bat. When started with no parameters the help screen is shown.
The SVNImporter takes 2 parameters for most actions: the command name and the config
file location. All the parameters are stored in the config file and most of them contain
explanatory comments. There are 3 types of parameters: for the target SVN, for the 
source repositories and general ones (files locations, logging). The default config 
file contains the configuration for all source repositories, but only the ones needed 
for selected repository are used for each run.

* 2.A GENERAL PARAMETERS
  FILE SETTINGS - spefify the locations of output files.
  
  LOG4J and DEBUG parameters don't need to be changed for normal running.

* 2.B SVN PARAMETERS
 There are 2 sets of SVN Parameters
 
 SVN DUMP OPTIONS - specify how the dump file is created. Eg. if TAG creation occurs in
 the CVS repository, it is transformed of copy into the tags_path in the dump file
 
 SVN AUTOIMPORT OPTIONS - specify how the dump file is imported. 
 
 import_dump_into_svn - if this parameter is present in the config file (is uncommented)
        the created dump is immediately imported to the SVN. Otherwise, it is only stored
        to file.
 
* 2.C CVS SOURCE PARAMETERS
 Set the srcprovider=cvs, the CVS parameters are stored in section CVS PROVIDER 
 CONFIGURATION.

* 2.D CVSRCS SOURCE PARAMETERS
 Set the srcprovider=cvsrcs, the CVSRCS parameters are stored in section CVSRCS PROVIDER
 CONFIGURATION.
 
* 2.E PVCS SOURCE PARAMETERS
 Set the srcprovider=pvcs, the PVCS parameters are stored in section PVCS PROVIDER 
 CONFIGURATION

* 2.F VSS SOURCE PARAMETERS
 Set the srcprovider=vss, the VSS parameters are stored in section VSS PROVIDER
 CONFIGURATION

* 2.G ClearCase SOURCE PARAMETERS
 Set the srcprovider=cc, the ClearCase parameters are stored in section CLEARCASE PROVIDER
 CONFIGURATION

* 2.H MKS SOURCE PARAMETERS
  Set the srcprovider=mks, the MKS parameters are stored in section MKS PROVIDER
  CONFIGURATION

* RUNNING THE EXPORT/IMPORT
During the exceution the SVNImporter writes information to svnimporter.log file,
that is usually good place to check when some problems occur.

  > run.bat list config.properties
To verify your config files settings, it is usefull to execute the list command, 
which only lists files in the source repository. The list is written to file
specified by list.files.to parameter.

  > run.bat full config.properties
This will create the full dump (=the whole source repository content). The resulting
dump is written to file specified by full.dump.file parameter. And is (depending on 
import_dump_into_svn parameter) loaded into the SVN.

3) TYPICAL USAGE SCENARIO

Edit config.properties
    - Set "srcprovider" to  "cvs", "cvsrcs", "pvcs", "mks", "cc", or "vss"
    - If you want one shot (not incremental) then set option "use_file_copy" to "yes"
    - Edit svn settings.
        If you run importer on Unix-like system then set
            svnadmin.executable="svnadmin"
            svnclient.executable="svn"
        Set svnadmin.tempdir to path to temporary directory
        Set svnadmin.repository_path to path to svn repository
    - Edit CVS settings. You must change following settings:
        cvs.username
        cvs.password
        cvs.hostname
        cvs.repository
        cvs.modulename
        cvs.tempdir
    - Or edit CVSRCS settings. You must change following settings:
        cvsrcs.repository_path
        cvsrcs.tempdir
    - Or edit PVCS settings. You must change following settings:
        pvcs.projectpath
        pvcs.tempdir
    - Or edit VSS settings. You must change following settings:
        vss.path
        vss.project
        vss.username
        vss.password
        vss.tempdir
    - Or edit ClearCase settings. You must change following settings:
        cc.projectpath
        cc.tempdir
    - Or edit MKS settings. You must change following settings:
        mks.project

Run svnimporter: > run.bat full config.properties

Svnimporter will
    * Create log file "svnimporter.log" in current directory
    * Create brief log file "history.log" in current directory
    * Create svn dump in file "full_dump_DATE.txt"
    * Create file "incr_history.txt" with history for incremetal dump (will be used later)
    * If SVN repository doesn't exist then it will be created
    * If SVN repository exists and path specified by "svnadmin.parent_dir" is already
        exists in repository then this path will be deleted
    * Import SVN dump "full_dump_DATE.txt" into SVN repository


4) REQUIREMENTS

SVNIMporter was tested on Java 1.4.2, compatible versions should work as well.
The java must be present on system path.


5) SOFTWARE VERSIONS

Application was tested on CVS version 1.11.2 and is compatible with other versions,
which have the same pserver protocol.

Application was tested on Merant PVCS version 8.0 and is compatible with other
versions, which have the same pcli utility command line interface.

Application was tested on VSS version 6.0 and is compatible with other versions,
which have the same ss utility command line interface.

Application was tested on ClearCase LT 2003 and is compatible with other versions,
which have the same cleartool utility command line interface.

Application was tested on MKS Source Integrity Enterprise Edition version 8.0
and is compatible with other versions, which have the same si utility command
line interface.


6) COMMUNITY, TROUBLESHOOTING AND FEEDBACK

For more information, forums, current builds check the SVNImporter project
page at http://polarion.org                          
