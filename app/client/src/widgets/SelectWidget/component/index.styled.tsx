import { Classes, ControlGroup, Label } from "@blueprintjs/core";
import styled from "styled-components";
import { Colors } from "constants/Colors";
import {
  FontStyleTypes,
  TextSize,
  TEXT_SIZES,
} from "constants/WidgetConstants";
import { DropdownOption } from "../constants";
import { Select } from "@blueprintjs/select";
import {
  BlueprintCSSTransform,
  createGlobalStyle,
} from "constants/DefaultTheme";
import { lightenColor } from "widgets/WidgetUtils";
import { DEFAULT_FONT_NAME } from "utils/hooks/useGoogleFont";
import { isEmptyOrNill } from ".";

export const TextLabelWrapper = styled.div<{
  compactMode: boolean;
}>`
  ${(props) =>
    props.compactMode ? "&&& {margin-right: 5px;}" : "width: 100%;"}
  display: flex;
`;

export const StyledDiv = styled.div`
  display: flex;
`;
export const StyledLabel = styled(Label)<{
  $compactMode: boolean;
  $disabled: boolean;
  $labelText?: string;
  $labelTextColor?: string;
  $labelTextSize?: TextSize;
  $labelStyle?: string;
}>`
  overflow-y: hidden;
  text-overflow: ellipsis;
  width: ${(props) => (props.$compactMode ? "auto" : "100%")};
  text-align: left;
  color: ${(props) =>
    props.$labelTextColor
      ? props.$labelTextColor
      : props.$disabled
      ? Colors.GREY_8
      : "inherit"};
  font-size: ${(props) =>
    props.$labelTextSize ? TEXT_SIZES[props.$labelTextSize] : "14px"};
  font-weight: ${(props) =>
    props?.$labelStyle?.includes(FontStyleTypes.BOLD) ? "bold" : "normal"};
  font-style: ${(props) =>
    props?.$labelStyle?.includes(FontStyleTypes.ITALIC) ? "italic" : ""};
`;

export const StyledControlGroup = styled(ControlGroup)`
  flex-grow: 1;

  &&& > {
    span {
      height: 100%;
      max-width: 100%;

      & > span {
        height: 100%;
      }
      .cancel-icon {
        svg {
          width: 10px !important;
          height: 10px !important;
        }
      }
      .dropdown-icon {
        width: 20px;

        svg {
          width: 20px;
          height: 20px;
        }
      }
    }
  }
`;

const SingleDropDown = Select.ofType<DropdownOption>();
export const StyledSingleDropDown = styled(SingleDropDown)<{
  backgroundColor: string;
  borderRadius: string;
  boxShadow?: string;
  primaryColor?: string;
  value: string;
  isValid: boolean;
  hasError?: boolean;
}>`
  div {
    flex: 1 1 auto;
  }
  span {
    width: 100%;
    position: relative;

    & > div {
      height: 100%;
    }
  }

  & .${Classes.BUTTON} {
    display: flex;
    width: 100%;
    height: 100%;
    align-items: center;
    justify-content: space-between;
    background: ${({ backgroundColor }) =>
      `${backgroundColor || Colors.WHITE}`} !important;
    border-radius: ${({ borderRadius }) => borderRadius} !important;
    box-shadow: ${({ boxShadow }) => `${boxShadow}`} !important;
    padding: 0px 10px;
    border: 1px solid;
    line-height: 30px;
    min-height: 32px;
    border-color: ${(props) =>
      props.hasError ? Colors.DANGER_SOLID : Colors.GREY_3};
    ${(props) =>
      props.isValid
        ? `
      &:hover { border: 1px solid ${Colors.GREY_5}; }
      &:focus { outline: 0; }
    `
        : ""};
  }

  & .${Classes.POPOVER_OPEN} .${Classes.BUTTON} {
    outline: 0;
    ${(props) =>
      !props.hasError
        ? `
      border: 1px solid ${props.primaryColor};
      box-shadow: 0px 0px 0px 3px ${lightenColor(
        props.primaryColor,
      )} !important;
    `
        : `border: 1px solid ${Colors.DANGER_SOLID};`}
  }

  & .${Classes.DISABLED} {
    background-color: ${Colors.GREY_1};
    border: 1px solid ${Colors.GREY_3};
    .${Classes.BUTTON_TEXT} {
      color: ${Colors.GREY_7};
    }
  }
  .${Classes.BUTTON_TEXT} {
    text-overflow: ellipsis;
    text-align: left;
    overflow: hidden;
    display: -webkit-box;
    -webkit-line-clamp: 1;
    -webkit-box-orient: vertical;
    color: ${(props) =>
      !isEmptyOrNill(props.value) ? Colors.GREY_10 : Colors.GREY_6};
    line-height: normal;
  }
  & {
    .${Classes.ICON} {
      width: fit-content;
      color: ${Colors.SLATE_GRAY};
    }
  }
`;

export const DropdownStyles = createGlobalStyle<{
  parentWidth: number;
  borderRadius: string;
  dropDownWidth: number;
  primaryColor?: string;
  fontFamily?: string;
  id: string;
}>`
${({ dropDownWidth, id, parentWidth }) => `
  .select-popover-width-${id} {
    min-width: ${parentWidth > dropDownWidth ? parentWidth : dropDownWidth}px;

    & .${Classes.INPUT_GROUP} {
       width: ${parentWidth > dropDownWidth ? parentWidth : dropDownWidth}px;
    }
  }
`}
  .select-popover-wrapper {
    width: auto;
    box-shadow: 0 6px 20px 0px rgba(0, 0, 0, 0.15) !important;
    background: white;
    border-radius: ${({ borderRadius }) =>
      borderRadius === `1.5rem` ? `0.375rem` : borderRadius} !important;
    font-family: ${({ fontFamily }) =>
      fontFamily === DEFAULT_FONT_NAME ? "inherit" : fontFamily} !important;
    overflow: hidden;

    & .${Classes.POPOVER_CONTENT} {
      background: transparent;
    }

    & .${Classes.INPUT_GROUP} {
      padding: 0;
      margin: 10px !important;

      & > .${Classes.ICON} {
        &:first-child {
          margin: 0 10px;
          height: 100%;
          display: flex;
          align-items: center;
          color: ${Colors.GREY_7};

          & > svg {
            width: 14px;
            height: 14px;
          }
        }
      }

      & > .${Classes.INPUT_ACTION} {
        &:last-child {
          margin: 0 10px;
          height: 100%;
          display: flex;
          align-items: center;
          color: ${Colors.GREY_7};

          .${Classes.BUTTON} {
            min-width: auto;
            margin: 0px;
            color: ${Colors.GREY_6} !important;

            &:hover {
              color: ${Colors.GREY_10} !important;
              background: ${Colors.GREY_2};
              border-radius: 0;
            }
          }
        }
      }

      .${Classes.INPUT} {
        height: 32px;
        padding-left: 32px;
        border: 1px solid ${Colors.GREY_3};
        color: ${Colors.GREY_10};
        border-radius: ${({ borderRadius }) => borderRadius} !important;
        &:focus {
          border: ${({ primaryColor }) => `1px solid ${primaryColor}`};
          box-shadow: 0px 0px 0px 3px ${({ primaryColor }) =>
            lightenColor(primaryColor)} !important;
          box-shadow: none;
        }
      }
    }
    & .${Classes.MENU} {
      max-width: 100% !important;
      max-height: auto;
      min-width: 0px !important;
      padding-top: 0px !important;
      border-radius: 0px;
    }

    & .${Classes.MENU_ITEM} {
      min-height: 38px;
      padding: 9px 12px;
      border-radius: 0px;
      color: ${Colors.GREY_8};
      &:hover{
        background: ${({ primaryColor }) => `${lightenColor(primaryColor)}`};
      }
      &.is-focused{
        background: ${({ primaryColor }) => `${lightenColor(primaryColor)}`};
      }
      &.${Classes.ACTIVE} {
        background: ${({ primaryColor }) => `${lightenColor(primaryColor)}`};
        color: ${Colors.GREY_10};
        position:relative;
      }
    }
  }
`;

export const DropdownContainer = styled.div<{ compactMode: boolean }>`
  ${BlueprintCSSTransform}
  display: flex;
  flex-direction: ${(props) => (props.compactMode ? "row" : "column")};
  align-items: ${(props) => (props.compactMode ? "center" : "left")};
  justify-content: end;
  gap: ${(props) => (props.compactMode ? "10px" : "5px")};

  label.select-label {
    margin-bottom: 0px;
    margin-right: 0px;
  }
`;
