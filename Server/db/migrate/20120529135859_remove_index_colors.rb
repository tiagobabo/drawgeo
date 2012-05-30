class RemoveIndexColors < ActiveRecord::Migration
  def up
  	remove_index "colors", :name => "index_colors_on_user_id"
  	drop_table :colors
  end

  def down
  end
end
