export interface UserDTO {
    userId: number;
    username : string;
    email: string;
    address?: string;
    profileImageUrl?: string;
    roles: string[];
}