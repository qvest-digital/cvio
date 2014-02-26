<!DOCTYPE html>
<html ng-app="cv">
  <head>
    <title>Profil verwalten</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link href="lib/bootstrap.css" rel="stylesheet" media="screen"/>

    <script src="lib/jquery.min.js"></script>
    <script src="lib/ui/jquery-ui.js"></script>
    <script src="lib/bootstrap.min.js"></script>
    <script src="lib/angular.min.js"></script>

    <script src="cv.js"></script>
  </head>
  <body>

    <div class="container" ng-controller="CvCtrl">
     <h1>Profil {{cv.givenName}} {{cv.familyName}}</h1>
      <blockquote>
        <p><small>Bitte beachten: Alle Angaben sind freiwillig. Mit dem Ausf&uuml;llen der 
            Daten best&auml;tigen Sie, dass sie damit Einverstanden diese Daten der tarent GmbH zur Nutzung zu überlassen.
            Die Nutzung beinhaltet sowohl die interne Verarbeitung in digitaler und nicht digitaler Form,
            sowie die Weiterreichung an (potenzielle) Kunden und Geschäftspartner der tarent GmbH.</small></p>
      </blockquote>

      <!-- Navigation -->
      <div>
        <a href="index.html"
           class="btn btn-default btn-md">
          <span class="glyphicon glyphicon-chevron-left"></span> &Uuml;ersicht
        </a>
        <button ng-click="save()" type="button" class="btn btn-default btn-md">
          <span ng-if="modified" class="glyphicon glyphicon-floppy-save text-danger"></span>
          <span ng-if="! modified" class="glyphicon glyphicon-floppy-saved text-success"></span>
          Speichern
        </button>
      </div>
      <br/>


      <!-- Skills -->
      <div ng-controller="SkillCtrl">
        <ul class="nav nav-pills">
          <li class="active"><a href="#" ng-click="selection = 'prog'" data-toggle="tab">Programmierung</a></li>
          <li><a href="#" ng-click="selection = 'tooling'" data-toggle="tab">Tools</a></li>
          <li><a href="#" ng-click="selection = 'test'" data-toggle="tab">Testing</a></li>
          <li><a href="#" ng-click="selection = 'paradigm'" data-toggle="tab">Konzepte</a></li>
        </ul>
        <div class="panel panel-default">
          <div id="all-items-box" class="connectedSortable panel-body">
            <div ng-repeat="item in ratingItems | filter:byCategory(selection)"
                 style="display: inline-block; margin: 4px; padding: 2px; padding-left: 6px; padding-right: 6px; border: solid 1px #cccccc; border-radius: 3px;">{{item.name}} <small><span style="color: #cccccc" class="glyphicon glyphicon-remove"></div>
            <div contenteditable="true" style="display: inline-block;  border: solid 1px #cccccc;"></div>
          </div>
        </div>
      </div>
      <div class="row">

        <div class="col-lg-4">
          <div class="panel panel-default" style="min-height: 300px">
            <div class="panel-heading clearfix">
              <span class="panel-title"><strong>Anfänger</strong></span>
              <span class="input-group pull-right" style="width: 180px;">
                <input type="text" class="input-sm form-control">
                <span class="input-group-btn">
                  <button class="input-sm btn btn-default" type="button">Hinzu</button>
                </span>
              </span>
            </div>
            <div class="connectedSortable panel-body" id="beginner-box">
            </div>
          </div>
        </div>

        <div class="col-lg-4">
          <div class="panel panel-default"  style="min-height: 300px">
            <div class="panel-heading clearfix">
              <span class="panel-title"><strong>Fortgeschritten</strong></span>
              <span class="input-group pull-right" style="width: 180px;">
                <input type="text" class="input-sm form-control">
                <span class="input-group-btn">
                  <button class="input-sm btn btn-default" type="button">Hinzu</button>
                </span>
              </span>
            </div>
            <div class="connectedSortable panel-body" id="advanced-box">
            </div>
          </div>
        </div>

        <div class="col-lg-4">
          <div class="panel panel-default"  style="min-height: 300px">
            <div class="panel-heading clearfix">
              <span class="panel-title"><strong>Experte</strong></span>
              <span class="input-group pull-right" style="width: 180px;">
                <input type="text" class="input-sm form-control">
                <span class="input-group-btn">
                  <button class="input-sm btn btn-default" type="button">Hinzu</button>
                </span>
              </span>
            </div>
            <div class="connectedSortable panel-body" id="expert-box">
            </div>
          </div>
        </div>

      </div>

      <!-- Allgemeine Daten -->
      <div class="panel panel-default">
        <div class="panel-heading">
          <h3 class="panel-title">Allgemeine Daten</h3>
        </div>
        <div class="panel-body">

          <form class="form-horizontal" role="form">
            
            <cv-short-field ng-model="cv.givenName" input-id="givenName" label="Vorname"></cv-short-field>
            <cv-short-field ng-model="cv.familyName" input-id="familyName" label="Nachname"></cv-short-field>
            <hr/>
            <cv-short-field ng-model="cv.placeOfBirth" input-id="placeOfBirth" label="Geburtsort-/Land"></cv-short-field>
            <cv-short-field ng-model="cv.languages" input-id="languages" label="Sprachen"></cv-short-field>
            <cv-short-field ng-model="cv.familyStatus" input-id="familyStatus" label="Familienstand/Kinder"></cv-short-field>
            <cv-short-field ng-model="cv.interests" input-id="interests" label="Interessen/Hobbys"></cv-short-field>
          </form>
        </div>   
      </div>

      <!-- Kontaktinfomationen -->
      <div class="panel panel-default">
        <div class="panel-heading">
          <h3 class="panel-title">Kontaktinfomationen</h3>
        </div>
        <div class="panel-body">

          <form class="form-horizontal" role="form">
            <cv-short-field ng-model="cv.email" input-id="email" label="E-Mail"></cv-short-field>
            <cv-short-field ng-model="cv.phoneMobile" input-id="phoneMobile" label="Telefon (Mobil)"></cv-short-field>
            <cv-short-field ng-model="cv.phoneFixed" input-id="phoneFixed" label="Telefon (Festnetz)"></cv-short-field>
            <cv-short-field ng-model="cv.xingProfile" input-id="xingProfile" label="Xing Profil"></cv-short-field>

            <hr/>
            <cv-short-field ng-model="cv.streetAddress" input-id="streetAddress" label="Stra&szlig;e, Hausnummer"></cv-short-field>
            <div class="form-group">
              <label for="postalcode" class="col-sm-2 control-label">PLZ/Ort</label>
              <div class="col-sm-2">
                <input type="text" class="form-control input-sm" id="postalcode" ng-model="cv.postalcode">
              </div>
              <div class="col-sm-3">
                <input type="text" class="form-control input-sm" id="locality" ng-model="cv.locality">
              </div>
            </div>
          </form>
        </div>   
      </div>

      <!-- Ausbildung -->
      <div class="panel panel-default">
        <div class="panel-heading">
          <div>
            <span class="panel-title"><strong>Ausbildung</strong></span>
            <button ng-click="addEducation()" type="button" class="btn btn-default btn-xs pull-right">
              <span class="glyphicon glyphicon-plus"></span> Ausbildung Einfügen
            </button>
          </div>
        </div>      
        <div class="panel-body">
          <form class="form-horizontal" role="form">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th></th>
                  <th>von</th>
                  <th>bis</th>
                  <th>Beschreibung</th>
                </tr>
              </thead>
              <tbody>
                <tr ng-repeat="education in cv.educations">
                  <td width="40"><button ng-click="deleteEducation(education)" type="button" class="btn btn-default btn-xs pull-right">
                      <span class="glyphicon glyphicon-trash"></span>
                  </button></td>
                  <td width="150">
                    <cv-date-field ng-model="education.start"></cv-date-field>
                  </td>
                  <td width="150">
                    <cv-date-field ng-model="education.end"></cv-date-field>
                    </div>
                  </td>
                  <td>
                    <div class="row" style="margin-bottom: 1px;">
                      <div class="col-sm-3" style="text-align: right;">Typ</div>
                      <div class="col-sm-3">
                        <select class="form-control input-sm" ng-model="education.type">     
                          <option value="school">Schule</option>
                          <option value="education">Ausbildung</option>
                          <option value="university">Uni/FH</option>
                          <option value="training">Fortbildung/Schulung</option>
                          <option value="researchProjects">Besondere Studienarbeit</option>
                          <option value="other">Sonstiges</option>
                        </select>
                      </div>
                    </div>
                    <cv-ce-form-row label="Title" ng-model="education.title" font-style="font-weight: bold;"></cv-ce-form-row>
                    <cv-ce-form-row label="Beschreibung" ng-model="education.description"></cv-ce-form-row>
                  </td>
                </tr>
              </tbody>
            </table>          
          </form>
        </div>
      </div>



      <!-- Projekt & Berufserfahrung -->
      <div class="panel panel-default">
        <div class="panel-heading">
          <div>
            <span class="panel-title"><strong>Projekt & Berufserfahrung</strong></span>
            <button ng-click="addJob()" type="button" class="btn btn-default btn-xs pull-right">
              <span class="glyphicon glyphicon-plus"></span> Projekt/Berufserfahrung einfügen
            </button>
          </div>
        </div>      
        <div class="panel-body">
          <form class="form-horizontal" role="form">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th></th>
                  <th>von</th>
                  <th>bis</th>
                  <th>Beschreibung</th>
                </tr>
              </thead>
              <tbody>
                <tr ng-repeat="job in cv.jobs">
                  <td width="40"><button ng-click="deleteJob(job)" type="button" class="btn btn-default btn-xs pull-right">
                      <span class="glyphicon glyphicon-trash"></span>
                  </button></td>
                  <td width="150">
                    <cv-date-field ng-model="job.start"></cv-date-field>
                  </td>
                  <td width="150">
                    <cv-date-field ng-model="job.end"></cv-date-field>
                    </div>
                  </td>
                  <td>
                    <div class="row" style="margin-bottom: 1px;">
                      <div class="col-sm-3" style="text-align: right;">Typ</div>
                      <div class="col-sm-3">
                        <select class="form-control input-sm" ng-model="job.type">     
                          <option value="employment">Anstellung</option>
                          <option value="employmedProject">Projekt als Angestellter</option>
                          <option value="company">Unternemerische T&auml;tigkeit</option>
                          <option value="freelance">Projekt als Freelancer</option>
                        </select>
                      </div>
                    </div>
                    <cv-ce-form-row label="Firma, Ort" ng-model="job.company" font-style="font-weight: bold;"></cv-ce-form-row>
                    <cv-ce-form-row label="Projekt" ng-model="job.project" font-style="font-weight: bold;"></cv-ce-form-row>
                    <cv-ce-form-row label="Beschreibung" ng-model="job.projectDescription"></cv-ce-form-row>
                    <cv-ce-form-row label="Rolle, Aufgaben" ng-model="job.role"></cv-ce-form-row>
                    <cv-ce-form-row label="Topics/Technologien" ng-model="job.topics"></cv-ce-form-row>
                  </td>
                </tr>
              </tbody>
            </table>          
          </form>
        </div>
      </div>


    </div>
  </body>
</html>
