import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IUserEventRegistration } from 'app/shared/model/user-event-registration.model';
import { UserEventRegistrationService } from './user-event-registration.service';

@Component({
    selector: 'jhi-user-event-registration-delete-dialog',
    templateUrl: './user-event-registration-delete-dialog.component.html'
})
export class UserEventRegistrationDeleteDialogComponent {
    userEventRegistration: IUserEventRegistration;

    constructor(
        private userEventRegistrationService: UserEventRegistrationService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.userEventRegistrationService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'userEventRegistrationListModification',
                content: 'Deleted an userEventRegistration'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-user-event-registration-delete-popup',
    template: ''
})
export class UserEventRegistrationDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ userEventRegistration }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(UserEventRegistrationDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.userEventRegistration = userEventRegistration;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
