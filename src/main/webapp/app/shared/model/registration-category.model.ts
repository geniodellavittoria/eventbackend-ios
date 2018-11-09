import { IUserEventRegistration } from 'app/shared/model//user-event-registration.model';

export interface IRegistrationCategory {
    id?: number;
    name?: string;
    userEventRegistrationIds?: IUserEventRegistration[];
}

export class RegistrationCategory implements IRegistrationCategory {
    constructor(public id?: number, public name?: string, public userEventRegistrationIds?: IUserEventRegistration[]) {}
}
