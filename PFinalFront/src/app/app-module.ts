import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { provideHttpClient } from '@angular/common/http';
import { AppRoutingModule } from './app-routing-module';
import { App } from './app';
import { LogIn } from './log-in/log-in';
import { Traductor } from './traductor/traductor';
import { Runcode } from './runcode/runcode';
import { Registro } from './registro/registro';
import { Perfil } from './perfil/perfil';
import { Admin } from './admin/admin';
import { AdminLogin } from './admin-login/admin-login';

@NgModule({
  declarations: [App, LogIn, Traductor, Runcode, Registro, Perfil, Admin, AdminLogin],
  imports: [BrowserModule, AppRoutingModule, FormsModule],
  providers: [provideHttpClient()],
  bootstrap: [App],
})
export class AppModule {}
