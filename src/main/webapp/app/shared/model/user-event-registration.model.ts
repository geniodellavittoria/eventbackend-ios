import { Moment } from 'moment';
import { IRegistrationCategory } from 'app/shared/model//registration-category.model';
import { IUser } from 'app/core/user/user.model';
import { IEvent } from 'app/shared/model//event.model';

export interface IUserEventRegistration {
    id?: number;
    timestamp?: Moment;
    registrationCategory?: IRegistrationCategory;
    userId?: IUser;
    event?: IEvent;
}

export class UserEventRegistration implements IUserEventRegistration {
    constructor(
        public id?: number,
        public timestamp?: Moment,
        public registrationCategory?: IRegistrationCategory,
        public userId?: IUser,
        public event?: IEvent
    ) {}
}
