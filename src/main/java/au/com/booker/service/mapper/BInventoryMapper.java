package au.com.booker.service.mapper;

import au.com.booker.domain.*;
import au.com.booker.service.dto.BInventoryDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link BInventory} and its DTO {@link BInventoryDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface BInventoryMapper extends EntityMapper<BInventoryDTO, BInventory> {



    default BInventory fromId(String id) {
        if (id == null) {
            return null;
        }
        BInventory bInventory = new BInventory();
        bInventory.setId(id);
        return bInventory;
    }
}
