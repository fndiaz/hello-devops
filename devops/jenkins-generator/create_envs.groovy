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

  job("create_envs") {
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
      numToKeep(10)
      artifactDaysToKeep(-1)
      artifactNumToKeep(-1)
    }

      steps {
      shell(getShell())
      }
  }
}


private String getShell() {

    String var_shell
    var_shell="""
cd devops/kubernetes
kubectl create -f service-hello-python.yml
kubectl create -f pv-mysql.yml
kubectl create -f configmap-script-mysql.yml
kubectl create -f deploy-mysql.yml
kubectl create -f deploy-rabbit.yml
kubectl create -f deploy-hello-python.yml
kubectl create -f service-mysql.yml
kubectl create -f service-rabbit.yml
kubectl create -f service-rabbit-web.yml
kubectl create -f deploy-hello-node.yml
sleep 20
ELB=\$(kubectl get services service-hello-python -o jsonpath="{.status.loadBalancer.ingress[0].hostname}")
until \$(curl --output /dev/null --silent --head --fail http://\$ELB); do
    printf '.'
    sleep 5
done
POD=\$(kubectl get pod -l app=mysql -o jsonpath="{.items[0].metadata.name}")
kubectl exec -ti \$POD sh /usr/local/mysql-init.sh
echo "URL: http://\$ELB"
"""

  return var_shell
}



