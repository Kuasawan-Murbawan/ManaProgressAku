package com.husyairi.ManaProgressAku.Service;

import com.husyairi.ManaProgressAku.DTO.ActivitySet.EditActivitySetRequest;
import com.husyairi.ManaProgressAku.DTO.ActivitySet.GetSetResponse;
import com.husyairi.ManaProgressAku.DTO.ActivitySet.InsertSetRequest;
import com.husyairi.ManaProgressAku.Entity.Model.ActivitySet;

import java.util.List;

public interface ActivitySetService {

    public GetSetResponse insertSet(InsertSetRequest request);

    public Long deleteSet(Long request);

    public GetSetResponse getSet(Long setID);

    public GetSetResponse editSet(InsertSetRequest request,Long setID);

    public List<ActivitySet> getAllSetByActivity(String activityID);

    public void deleteAllSetByActivity(String activityID);

    public String editActivitySet(EditActivitySetRequest request);
}
