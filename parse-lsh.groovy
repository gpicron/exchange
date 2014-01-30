def history = new File("/auto/home/pon/ENV/git-repo/.git/lshistory.bak")

def count = 0

def rhistory = [] 
def last = null
history.eachLine { line -> 
if (count++ % 1000 == 0) println count 
        if (line ==~ /^.+\|\d+\.\d+\|.+/ ) {
	  if (last != null) {
	    rhistory.add(0, last)
	  } 
	  last = line
	} else {
	  last += '\n' + line
        }
}
rhistory.add(0, last)


count = 0

rhistory.each {
    if (count++ < 10) {
  println it
  def record = (it =~ /(?ms)^(.+)\|(\d+\.\d+)\|(.+)\|(.+)\|(.+)\|(.+)$/)
  println record
  println "A:" + record[0][1] 
  println "TS:" + record[0][2]
  println "U:" + record[0][3]
  println "O:" + record[0][4]
  println "V:" + record[0][5]
  println "C:" + record[0][6]
  println "----"
    } 
}  
