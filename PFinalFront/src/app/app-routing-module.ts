import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LogIn } from './log-in/log-in';
import { Traductor } from './traductor/traductor';
import { Runcode } from './runcode/runcode';
import { Registro } from './registro/registro';
import { Perfil } from './perfil/perfil';
import { Admin } from './admin/admin';
import { AdminLogin } from './admin-login/admin-login';
import { Auditoria } from './auditoria/auditoria';

const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LogIn },
  { path: 'registro', component: Registro },
  { path: 'traductor', component: Traductor },
  { path: 'runcode', component: Runcode },
  { path: 'perfil', component: Perfil },
  { path: 'admin', component: Admin },
  { path: 'admin-login', component: AdminLogin },
  { path: 'auditoria', component: Auditoria },
  { path: '**', redirectTo: '/login' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
