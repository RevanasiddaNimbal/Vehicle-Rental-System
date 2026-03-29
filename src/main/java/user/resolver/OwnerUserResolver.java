package user.resolver;

import exception.ResourceNotFoundException;
import user.model.UserDetails;
import user.model.UserRole;
import vehicleowner.models.VehicleOwner;
import vehicleowner.service.VehicleOwnerService;

public class OwnerUserResolver implements UserTypeResolver {
    private final VehicleOwnerService vehicleOwnerService;

    public OwnerUserResolver(VehicleOwnerService vehicleOwnerService) {
        this.vehicleOwnerService = vehicleOwnerService;
    }

    @Override
    public UserDetails resolve(String userId) {
        VehicleOwner owner = vehicleOwnerService.getVehicleOwnerById(userId);
        if (owner == null) {
            throw new ResourceNotFoundException("Owner ID :" + userId + " not found.");
        }
        return new UserDetails(owner.getName(), owner.getEmail(), owner.getPhone(), UserRole.OWNER);
    }
}