package br.com.karlosimoreira.fcvarzea.domain.util;

import java.util.ArrayList;

/**
 * Created by Carlos on 01/07/2016.
 */
public class SelectableImageGroup {

    private ArrayList<SelectableImage> list;
    private SelectableImage selectedImage;

    public SelectableImageGroup(){
        list = new ArrayList<>();
    }

    public void add(SelectableImage selectableImage){
        list.add(selectableImage);
    }

    public void setSelectedImage(SelectableImage selectedImage){
        if(this.selectedImage == selectedImage)return;
        if(!list.contains(selectedImage))throw new IndexOutOfBoundsException();
        if (this.selectedImage != null) {
            this.selectedImage.setSelected(false);
        }
        selectedImage.setSelected(true);
        this.selectedImage = selectedImage;
    }

    public void setSelectedPosition(int position){
        SelectableImage selectedImage = list.get(position);
        setSelectedImage(selectedImage);
    }

    public ArrayList<SelectableImage> getArray(){
        return list;
    }
}
