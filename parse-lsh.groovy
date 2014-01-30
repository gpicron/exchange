import groovy.transform.ToString;

import java.util.zip.GZIPInputStream;
import java.util.regex.Matcher;

gunzip = { File src,File dest->

	def input = new GZIPInputStream(src.newDataInputStream())
	def output = dest.newDataOutputStream()

	output << input

	input.close()
	output.close()
}



def history = new File("lshistory.bak.tail")

gunzip (new File("/Users/gpicron/Documents/bull/eurocontrol/ENV/exchange/lshistory.bak.gz"), new File("lshistory.bak"))


def output = history.newDataOutputStream()
def proc = ("/usr/bin/tail -n 100000 " + new File("lshistory.bak").absolutePath).execute();

output << proc.in

output.close()

enum CCObjectType {
	FILE,
	DIRECTORY,
	DIRECTORY_VERSION,
	BRANCH,
	VERSION,
	SYMBOLIC_LINK
	
	static CCObjectType extValueOf(String text) {
		switch (text) {
			case "file element": 
				return FILE
			case "directory element":
				return DIRECTORY
			case "directory version":			
				return DIRECTORY_VERSION
			case "symbolic link":
				return SYMBOLIC_LINK
			default:
				return CCObjectType.valueOf(text.toUpperCase())
		}
	}
}


enum CCAction {
	MAKE_ELEMENT,
	CHECK_IN,
	CHECK_OUT,
	MAKE_LINK
}


@ToString(includeNames=true)
class CCEvent {
	CCAction action
	CCObjectType objectType
	
	Date timestamp
	String user
	String object
	String branch
	String version
	String comment

	CCEvent(String line) {
		def record = (line =~ /(?ms)^(.+)\|(\d+\.\d+)\|(.+)\|(.+)\|(.*)\|(.*)$/)
		switch (record[0][1]) {
			case ~(/^mkelem(.*)/) :
			  action = CCAction.MAKE_ELEMENT
			  break;
			case ~(/^checkin(.*)/) :
			  action = CCAction.CHECK_IN
			  break;
			case ~(/^checkout(.*)/) :
			  action = CCAction.CHECK_OUT
			  break;
			case ~(/^mkslink(.*)/) :
			  action = CCAction.MAKE_LINK
			  break;
			case ~(/^mkbranch(.*)/) :
			  action = CCAction.CHECK_IN
			  break;
			default:
				throw new Exception("unknown action ${record[0][1]}\n$line")
		}
		objectType = CCObjectType.extValueOf(Matcher.lastMatcher[0][1]);
		
		timestamp = Date.parse("yyyyMMdd.HHmmss", record[0][2]);
		user = record[0][3]
		object = record[0][4]
		branch = record[0][5]
	    def bv = (branch =~ /^(.*)\/(\d+)?$/ );
		if (bv) {
			branch = bv[0][1]
			version = bv[0][2]
		}
		comment = record[0][6]
	}
}


class CCHistory {
	Collection events = []
	
	CCHistory(input) {
		def last = null
		input.eachLine { line ->
			if (line ==~ /^.+\|\d+\.\d+\|.+/ ) {
				if (last != null) {
					events.add(0, new CCEvent(last))
				}
				last = line
			} else {
				last += '\n' + line
			}
		}
		events.add(0, new CCEvent(last))
		
		events.sort { a, b ->
			a.timestamp.compareTo(b.timestamp)
		}
	} 
		
}

def rhistory = new CCHistory(history) 

rhistory.events = rhistory.events.findAll { it.action == CCAction.CHECK_IN  }

def count = 0


@ToString(includeNames=true)
class DirectoryCreation {
	String branch
	Long version
	String path
}

@ToString(includeNames=true)
class FileCreation {
	String branch
	Long version
	String path
	String comment
}

def current = [:]

@ToString(includeNames=true)
class ChangeSet {
	String user
	String comment
	String branch
	
	Collection changes = []
	Map versions = [:]
	
}


def currentChangeSet = [:]

def changeSets = []


rhistory.events.each { event ->
	println "--- " + event
		 
}
