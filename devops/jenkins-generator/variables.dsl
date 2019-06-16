app_job {
  'hello-python' {
    url = 'https://github.com/fndiaz/hello-devops'
    branch = 'master'
    rotate_builds = 10
    pos_build = 'deploy-hello-node'
    credentials = 'dockerhub'
    user_dockerhub = 'fndiaz'
  }

  'hello-node' {
    url = 'https://github.com/fndiaz/hello-devops'
    branch = 'master'
    rotate_builds = 10
    pos_build = 'deploy-hello-node'
    credentials = 'dockerhub'
    user_dockerhub = 'fndiaz'
  }

}

list_views {
  'View Builds'{
    description = "builds jobs"
    regex = '.*build.*'
  }
  'View Deploy'{
    description = "deploy jobs"
    regex = '.*deploy.*'
  }
}