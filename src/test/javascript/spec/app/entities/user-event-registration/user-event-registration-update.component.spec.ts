/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { EventAppTestModule } from '../../../test.module';
import { UserEventRegistrationUpdateComponent } from 'app/entities/user-event-registration/user-event-registration-update.component';
import { UserEventRegistrationService } from 'app/entities/user-event-registration/user-event-registration.service';
import { UserEventRegistration } from 'app/shared/model/user-event-registration.model';

describe('Component Tests', () => {
    describe('UserEventRegistration Management Update Component', () => {
        let comp: UserEventRegistrationUpdateComponent;
        let fixture: ComponentFixture<UserEventRegistrationUpdateComponent>;
        let service: UserEventRegistrationService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [EventAppTestModule],
                declarations: [UserEventRegistrationUpdateComponent]
            })
                .overrideTemplate(UserEventRegistrationUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(UserEventRegistrationUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(UserEventRegistrationService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new UserEventRegistration(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.userEventRegistration = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new UserEventRegistration();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.userEventRegistration = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.create).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));
        });
    });
});
