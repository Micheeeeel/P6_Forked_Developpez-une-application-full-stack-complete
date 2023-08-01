import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { SubjectsComponent } from './pages/subjects/subjects.component';
import { SubjectFormComponent } from './pages/subjectform/subjectform.component';
import { SubjectDetailComponent } from './pages/home/subject-detail/subject-detail.component';

// consider a guard combined with canLoad / canActivate route option
// to manage unauthenticated user to access private routes
const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'subjects', component: SubjectsComponent },
  { path: 'subject-form', component: SubjectFormComponent },
  { path: 'subject-form/:id', component: SubjectFormComponent },
  { path: 'subject/:id', component: SubjectDetailComponent },

  // Redirection en cas d'URL incorrecte (404 Not Found)
  { path: '**', redirectTo: '' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
