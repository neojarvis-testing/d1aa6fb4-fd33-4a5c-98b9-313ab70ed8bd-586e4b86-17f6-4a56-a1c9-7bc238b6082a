import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { AdminAddArchitectComponent } from './components/admin-add-architect/admin-add-architect.component';
import { AdminControlPanelComponent } from './components/admin-control-panel/admin-control-panel.component';
import { AdminEditArchitectComponent } from './components/admin-edit-architect/admin-edit-architect.component';
import { AdminViewArchitectComponent } from './components/admin-view-architect/admin-view-architect.component';
import { AdminViewFeedbackComponent } from './components/admin-view-feedback/admin-view-feedback.component';
import { AdminViewInquiryComponent } from './components/admin-view-inquiry/admin-view-inquiry.component';
import { AdminnavComponent } from './components/adminnav/adminnav.component';
import { AuthguardComponent } from './components/authguard/authguard.component';
import { ErrorComponent } from './components/error/error.component';
import { HomePageComponent } from './components/home-page/home-page.component';
import { LoginComponent } from './components/login/login.component';
import { SignupComponent } from './components/signup/signup.component';
import { UserAddFeedbackComponent } from './components/user-add-feedback/user-add-feedback.component';
import { UserAddInquiryComponent } from './components/user-add-inquiry/user-add-inquiry.component';
import { UserViewArchitectComponent } from './components/user-view-architect/user-view-architect.component';
import { UserViewFeedbackComponent } from './components/user-view-feedback/user-view-feedback.component';
import { UserViewInquiriesComponent } from './components/user-view-inquiries/user-view-inquiries.component';
import { UsernavComponent } from './components/usernav/usernav.component';
import { HttpClientModule } from '@angular/common/http';

@NgModule({
  declarations: [
    AppComponent,
    AdminAddArchitectComponent,
    AdminControlPanelComponent,
    AdminEditArchitectComponent,
    AdminViewArchitectComponent,
    AdminViewFeedbackComponent,
    AdminViewInquiryComponent,
    AdminnavComponent,
    AuthguardComponent,
    ErrorComponent,
    HomePageComponent,
    LoginComponent,
    SignupComponent,
    UserAddFeedbackComponent,
    UserAddInquiryComponent,
    UserViewArchitectComponent,
    UserViewFeedbackComponent,
    UserViewInquiriesComponent,
    UsernavComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
