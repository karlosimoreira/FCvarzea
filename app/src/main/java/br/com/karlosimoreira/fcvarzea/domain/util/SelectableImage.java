package br.com.karlosimoreira.fcvarzea.domain.util;

/**
 * Created by Carlos on 01/07/2016.
 */
public class SelectableImage {

    private final int selectedImageResource;
    private final int unSelectedImageResource;
    private boolean isSelected;

    public SelectableImage(int selectedImageResource, int unSelectedImageResource){

        this.selectedImageResource = selectedImageResource;
        this.unSelectedImageResource = unSelectedImageResource;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getImageResource(){
        return isSelected ? selectedImageResource : unSelectedImageResource;
    }
}
