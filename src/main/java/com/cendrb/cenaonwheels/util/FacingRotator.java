package com.cendrb.cenaonwheels.util;

import net.minecraft.util.EnumFacing;

/**
 * Created by cendr_000 on 03.04.2016.
 */
public class FacingRotator {

    private EnumFacing facingForward;

    public FacingRotator(EnumFacing facingForward)
    {
        this.facingForward = facingForward;
    }

    public EnumFacing getAbsoluteDirection(RelativeDirection relativeDirection)
    {
        return getAbsoluteDirection(facingForward, relativeDirection);
    }

    public static EnumFacing getAbsoluteDirection(EnumFacing facingForward, RelativeDirection relativeDirection)
    {
        switch (facingForward)
        {
            case NORTH:
                switch (relativeDirection)
                {
                    case FORWARD:
                        return EnumFacing.NORTH;
                    case BACKWARD:
                        return EnumFacing.SOUTH;
                    case LEFT:
                        return EnumFacing.WEST;
                    case RIGHT:
                        return EnumFacing.EAST;
                }
                break;
            case SOUTH:
                switch (relativeDirection)
                {
                    case FORWARD:
                        return EnumFacing.SOUTH;
                    case BACKWARD:
                        return EnumFacing.NORTH;
                    case LEFT:
                        return EnumFacing.WEST;
                    case RIGHT:
                        return EnumFacing.EAST;
                }
                break;
            case WEST:
                switch (relativeDirection)
                {
                    case FORWARD:
                        return EnumFacing.WEST;
                    case BACKWARD:
                        return EnumFacing.EAST;
                    case LEFT:
                        return EnumFacing.SOUTH;
                    case RIGHT:
                        return EnumFacing.NORTH;
                }
                break;
            case EAST:
                switch (relativeDirection)
                {
                    case FORWARD:
                        return EnumFacing.EAST;
                    case BACKWARD:
                        return EnumFacing.WEST;
                    case LEFT:
                        return EnumFacing.NORTH;
                    case RIGHT:
                        return EnumFacing.SOUTH;
                }
                break;
        }
        return null;
    }
}
