/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EventAppTestModule } from '../../../test.module';
import { UserEventRegistrationDetailComponent } from 'app/entities/user-event-registration/user-event-registration-detail.component';
import { UserEventRegistration } from 'app/shared/model/user-event-registration.model';

describe('Component Tests', () => {
    describe('UserEventRegistration Management Detail Component', () => {
        let comp: UserEventRegistrationDetailComponent;
        let fixture: ComponentFixture<UserEventRegistrationDetailComponent>;
        const route = ({ data: of({ userEventRegistration: new UserEventRegistration(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [EventAppTestModule],
                declarations: [UserEventRegistrationDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(UserEventRegistrationDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(UserEventRegistrationDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.userEventRegistration).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
