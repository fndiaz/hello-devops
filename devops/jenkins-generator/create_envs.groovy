//def slurper = new ConfigSlurper()
// fix classloader problem using ConfigSlurper in job dsl
//slurper.classLoader = this.class.classLoader
//def config = slurper.parse(readFileFromWorkspace('deploy.dsl'))

//config.app_job.each { name, data ->
//  println "generating application $name"
//  println name
  //println data
createJob("https://github.com/fndiaz/hello-devops.git")
//}


def createJob(repo){

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

      steps {
      shell(getShell(data))
      }
  }
}


private String getShell(data) {

    String var_shell
    var_shell="""
cd devops/kubernetes
kubectl create -f configmap-script-mysql.yml
kubectl create -f pv-mysql.yml
kubectl create -f deploy-mysql.yml
kubectl create -f deploy-rabbit.yml
kubectl create -f deploy-hello-python.yml
kubectl create -f deploy-hello-node.yml
kubectl create -f service-mysql.yml
kubectl create -f service-rabbit.yml
kubectl create -f service-rabbit-web.yml
kubectl create -f service-hello-python
kubectl get svc service-hello-python"""

  return var_shell
}



