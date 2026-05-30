package com.husyairi.ManaProgressAku.Service;

import com.husyairi.ManaProgressAku.DTO.ActivitySet.GetSetResponse;
import com.husyairi.ManaProgressAku.DTO.ActivitySet.InsertSetRequest;

public interface ActivitySetService {

    public GetSetResponse insertSet(InsertSetRequest request);

    public Long deleteSet(Long request);

    public GetSetResponse getSet(Long setID);

    public GetSetResponse editSet(InsertSetRequest request,Long setID);
}
