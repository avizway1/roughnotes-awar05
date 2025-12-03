terraform {
  cloud {
    organization = "aviz_organisation"
    workspaces {
      name = "tf-aviz"
    }
  }
}