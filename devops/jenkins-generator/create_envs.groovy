job("DSL-Tutorial-2-Test") {

  scm {
    git {
      remote {
        url("git@bitbucket.org:teste/n.git")
      }
      branch("master")
    }
  }
  
  publishers {
    //downstream("testes-ui-homol", "success")
      buildPipelineTrigger('rbenv')
  }
  
  logRotator {
        numToKeep(5)
        artifactNumToKeep(1)
    }
  
  
        triggers {
              cron('') 
            }
     
      wrappers {
            rbenv('2.1.2') {
                ignoreLocalVersion()
                gems('bundler', 'rake')
            }
      }

}