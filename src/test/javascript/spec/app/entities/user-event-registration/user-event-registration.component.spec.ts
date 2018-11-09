/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { EventAppTestModule } from '../../../test.module';
import { UserEventRegistrationComponent } from 'app/entities/user-event-registration/user-event-registration.component';
import { UserEventRegistrationService } from 'app/entities/user-event-registration/user-event-registration.service';
import { UserEventRegistration } from 'app/shared/model/user-event-registration.model';

describe('Component Tests', () => {
    describe('UserEventRegistration Management Component', () => {
        let comp: UserEventRegistrationComponent;
        let fixture: ComponentFixture<UserEventRegistrationComponent>;
        let service: UserEventRegistrationService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [EventAppTestModule],
                declarations: [UserEventRegistrationComponent],
                providers: []
            })
                .overrideTemplate(UserEventRegistrationComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(UserEventRegistrationComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(UserEventRegistrationService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new UserEventRegistration(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.userEventRegistrations[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
