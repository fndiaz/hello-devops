def slurper = new ConfigSlurper()
// fix classloader problem using ConfigSlurper in job dsl
slurper.classLoader = this.class.classLoader
def config = slurper.parse(readFileFromWorkspace('devops/jenkins-generator/build_jobs.dsl'))

config.app_job.each { name, data ->
  println "generating build $name"
  //println name
  //println data
  createJob(name, data)
}

config.list_views.each { name, data ->
  println "generating view $name"
  //println name
  //println data
  createView(name, data)
}




def createJob(app, data){

	job("build-${app}") {
		scm {
	    	git {
				remote {
	            	url(data.url)
	            }
	        	branch(data.branch)
	    	}
		}

		logRotator {
			daysToKeep(-1)
			numToKeep(data.rotate_builds)
			artifactDaysToKeep(-1)
			artifactNumToKeep(-1)
		}

		wrappers {
            credentialsBinding {
            usernamePassword('DOCKERUSER', 'DOCKERPASS', 'dockerhub')
        }
    	}

	    steps {
			shell(getShell(app))
	    }

        try {
		    publishers {
				downstream("deploy-${app}", "SUCCESS")
			}
        }
        catch (MissingPropertyExceptionmpe) {
        	println 'pos build nao configurado'
        }


        try {
        	triggers {
            	cron(data."${enviroment}".cron)
            }
        }
        catch (MissingPropertyExceptionmpe) {
        	println 'cron nao configurado'
        }

	}
}


private String getShell(app) {

    String var_shell
    var_shell="""
cd hello-python
docker build -t fndiaz/hello-python:\$GIT_COMMIT .
echo \$DOCKERUSER
echo \$DOCKERPASS
docker login -u $DOCKERUSER -p \$DOCKERPASS
docker push ${user_dockerhub}/${app}:\$GIT_COMMIT"""

 	return var_shell
}


def createView(app, data) {

	listView("${app}") {
	    description("${data.description}")
	    filterBuildQueue()
	    filterExecutors()
	    jobs {
	        //name('release-projectA')
	        regex(/${data.regex}/)
	    }
	    columns {
	        status()
	        weather()
	        name()
	        lastSuccess()
	        lastFailure()
	        lastDuration()
	        buildButton()
	    }
	}

}



