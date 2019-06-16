//def slurper = new ConfigSlurper()
// fix classloader problem using ConfigSlurper in job dsl
//slurper.classLoader = this.class.classLoader
//def config = slurper.parse(readFileFromWorkspace('deploy.dsl'))

//config.app_job.each { name, data ->
//  println "generating application $name"
//  println name
  //println data
println "init"
createJob("https://github.com/fndiaz/hello-devops.git")
//}


def createJob(repo){
  println "run.."
  job("create-envs") {
    scm {
        git {
        remote {
                url(repo)
              }
            branch("master")
        }
    }

    logRotator {
      daysToKeep(-1)
      numToKeep(data.rotate_builds)
      artifactDaysToKeep(-1)
      artifactNumToKeep(-1)
    }
      println "pre shell"
      steps {
      shell("teste")
      }
      println "pos shell"
  }
}


