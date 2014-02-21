<!DOCTYPE html>
<html ng-app="cv">
  <head>
    <title>Profil verwalten</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link href="lib/bootstrap.css" rel="stylesheet" media="screen"/>

    <script src="lib/jquery.min.js"></script>
    <script src="lib/bootstrap.min.js"></script>
    <script src="lib/angular.min.js"></script>

    <script src="cv.js"></script>
  </head>
  <body>

    <div class="container" ng-controller="CvCtrl">
     <h1>Profil {{cv.givenName}} {{cv.familyName}}</h1>
      <blockquote>
        <p><small>Bitte beachten: Alle Angaben sind freiwillig. Mit dem Ausf&uuml;llen der 
            Daten best&auml;tigen Sie, dass sie damit Einverstanden diese Daten der tarent zur Nutzung zu überlassen.
            Die Nutzung beinhaltet sowohl die interne Veranrbeitung in digitaler und nicht digitaler Form,
            sowie die Weiterreichung an (potenzielle) Kunden und Geschäftspartner der tarent.</small></p>
      </blockquote>

      <div> <!-- navigation -->
        <a href="index.html"
           class="btn btn-default btn-md">
          <span class="glyphicon glyphicon-chevron-left"></span> Übersicht
        </a>
        <button ng-click="save()" type="button" class="btn btn-default btn-md">
          <span ng-if="modified" class="glyphicon glyphicon-floppy-save text-danger"></span>
          <span ng-if="! modified" class="glyphicon glyphicon-floppy-saved text-success"></span>
          Speichern
        </button>
      </div>
      <br/>

      <div class="panel panel-default">
        <div class="panel-heading">
          <h3 class="panel-title">Allgemeine Daten</h3>
        </div>
        <div class="panel-body">

          <form class="form-horizontal" role="form">
            
            <cv-short-field ng-model="cv.givenName" input-id="givenName" label="Vorname"></cv-short-field>

            <cv-short-field ng-model="cv.familyName" input-id="familyName" label="Nachname"></cv-short-field>

            <cv-short-field ng-model="cv.placeOfBirth" input-id="placeOfBirth" label="Geburtsort-/Land"></cv-short-field>

            <cv-short-field ng-model="cv.familyStatus" input-id="familyStatus" label="Familienstand/Kinder"></cv-short-field>

            <cv-short-field ng-model="cv.streetAddress" input-id="streetAddress" label="Stra&szlig;e, Hausnummer"></cv-short-field>

            <div class="form-group">
              <label for="postalcode" class="col-lg-4 control-label">PLZ/Ort</label>
              <div class="col-lg-2">
                <input type="text" class="form-control" id="postalcode" ng-model="cv.postalcode">
              </div>
              <div class="col-lg-3">
                <input type="text" class="form-control" id="locality" ng-model="cv.locality">
              </div>
            </div>
          </form>
        </div>   
      </div>


      <div class="panel panel-default">
        <div class="panel-heading">
          <div>
            <span class="panel-title"><strong>Projekt & Berufserfahrung</strong></span>
            <button ng-click="addJob()" type="button" class="btn btn-default btn-xs pull-right">
              <span class="glyphicon glyphicon-plus"></span> Neu
            </button>
          </div>
        </div>

        <div class="panel-body">
          <table class="table table-striped">
            <thead>
              <tr>
                <th></th>
                <th>von</th>
                <th>bis</th>
                <th>Typ</th>
                <th>Beschreibung</th>
              </tr>
            </thead>
            <tbody>
              <tr ng-repeat="job in cv.jobs">
                <td><button ng-click="deleteJob()" type="button" class="btn btn-default btn-xs pull-right">
                    <span class="glyphicon glyphicon-trash"></span>
                  </button></td>
                <td>{{job.start}}</td>
                <td>{{job.end}}</td>
                <td>{{job.type}}</td>
                <td>
                  <strong><div contenteditable ng-model="job.company"></div></strong>
                  <strong><div contenteditable ng-model="job.project"></div></strong>
                  <div contenteditable ng-model="job.projectDescription"></div>
                  <div contenteditable ng-model="job.role"></div>
                  <div contenteditable ng-model="job.activity"></div>
                  <div contenteditable ng-model="job.topics"></div>
                </td>
              </tr>
            </tbody>
          </table>          
        </div>
      </div>




      <div class="panel panel-default">
        <div class="panel-heading">
          <div>
            <span class="panel-title"><strong>Projekt & Berufserfahrung</strong></span>
            <button ng-click="addJob()" type="button" class="btn btn-default btn-xs pull-right">
              <span class="glyphicon glyphicon-plus"></span> Neu
            </button>
          </div>
        </div>      
        <div class="panel-body">
          <table class="table table-striped">
            <thead>
              <tr>
                <th></th>
                <th>von</th>
                <th>bis</th>
                <th>Typ</th>
                <th>Beschreibung</th>
              </tr>
            </thead>
            <tbody>
              <tr ng-repeat="job in cv.jobs">
                <td><button ng-click="deleteJob(job)" type="button" class="btn btn-default btn-xs pull-right">
                    <span class="glyphicon glyphicon-trash"></span>
                  </button></td>
                <td>{{job.start}}</td>
                <td>{{job.end}}</td>
                <td>{{job.type}}</td>
                <td>
                  <div class="row" style="margin-bottom: 1px;">
                    <div class="col-lg-3" style="text-align: right;">Firma</div>
                    <strong><div class="col-lg-9 bg-warningXX" contenteditable ng-model="job.company" style="min-height: 24px; max-width: 450px;"></div></strong>
                  </div>
                  <div class="row" style="margin-bottom: 1px;">
                    <div class="col-lg-3" style="text-align: right;">Projekt</div>
                    <strong><div  class="col-lg-9 bg-warningXX" contenteditable ng-model="job.project" style="min-height: 24px; max-width: 450px;"></div></strong>
                  </div>
                  <div class="row" style="margin-bottom: 1px;">
                    <div class="col-lg-3" style="text-align: right;">Beschreibung</div>
                    <div  class="col-lg-9 bg-warningXX" contenteditable ng-model="job.projectDescription" style="min-height: 24px; max-width: 450px;"></div>
                  </div>
                  <div class="row" style="margin-bottom: 1px;">
                    <div class="col-lg-3" style="text-align: right;">Rolle</div>
                    <div  class="col-lg-9 bg-warningXX" contenteditable ng-model="job.role" style="min-height: 24px; max-width: 450px;"></div>
                  </div>
                  <div class="row" style="margin-bottom: 1px;">
                    <div class="col-lg-3" style="text-align: right;">Aktivitäten</div>
                    <div  class="col-lg-9 bg-warningXX" contenteditable ng-model="job.activity" style="min-height: 24px; max-width: 450px;"></div>
                  </div>
                  <div class="row" style="margin-bottom: 1px;">
                    <div class="col-lg-3" style="text-align: right;">Topics/Technologien</div>
                    <div  class="col-lg-9 bg-warningXX" contenteditable ng-model="job.topics" style="min-height: 24px; max-width: 450px;"></div>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>          
        </div>
      </div>


    </div>
  </body>
</html>
